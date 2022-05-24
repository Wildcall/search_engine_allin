import axios, {AxiosInstance} from "axios";
import {ApiConst} from "@/api/ApiConst";
import {StorageService} from "@/services/StorageService";
import {useAuthStore} from "@/store/AuthStore";
import {SseParam} from "@/models/SseParam";
import {ResourceType} from "@/models/ResourceType";

export class Api {

    static getAuth(clear: boolean): AxiosInstance {
        const instance = axios.create({
            baseURL: ApiConst.API_AUTH
        })
        if (clear) return instance
        return this.setupResponseInterceptor(instance)
    }

    static getTask(): AxiosInstance {
        const instance = axios.create({
            baseURL: ApiConst.API_TASK
        })

        return this.setupRequestInterceptor(this.setupResponseInterceptor(instance))
    }

    static getSearch(): AxiosInstance {
        return axios.create({
            baseURL: ApiConst.API_SEARCH
        })
    }

    static getSse(sseParam: SseParam, type: ResourceType): EventSource {
        let query: string = {} as string
        if (type === ResourceType.CRAWLER) query = ApiConst.API_CRAWLER_SSE
        if (type === ResourceType.INDEXER) query = ApiConst.API_INDEXER_SSE
        if (type === ResourceType.SEARCHER) query = ApiConst.API_SEARCHER_SSE

        return new EventSource(`${query}?siteId=${sseParam.siteId}&appUserId=${sseParam.appUserId}`)
    }

    static getPageSse(sseParam: SseParam): EventSource {
        const query = `${ApiConst.API_PAGE_SSE}?siteId=${sseParam.siteId}&appUserId=${sseParam.appUserId}`
        return new EventSource(query)
    }

    private static setupResponseInterceptor(instance: AxiosInstance): AxiosInstance {
        instance.interceptors.response.use((response) => response,
            (error) => {
                const originalConfig = error.config
                if (error.response.status === 401 && !originalConfig._retry) {
                    if (originalConfig.url === ApiConst.API_AUTH + '/user/refresh') {
                        console.log("Refresh token was expired")
                        useAuthStore().logout()
                        return Promise.reject("Refresh token was expired")
                    }
                    originalConfig._retry = true
                    console.log("Need to update Access Token")
                    const token = StorageService.getLocalRefreshToken()
                    if (token) {
                        return axios.create({baseURL: ApiConst.API_AUTH})
                            .get(ApiConst.API_AUTH + '/user/refresh', {headers: {'Authorization': `Bearer ${token}`}})
                            .then((response) => {
                                const accessToken = response?.data?.access_token
                                if (accessToken) {
                                    StorageService.updateLocalAccessToken(accessToken)
                                    originalConfig.headers['Authorization'] = `Bearer ${accessToken}`
                                    return instance.request(originalConfig)
                                }
                            })
                    }

                }
                return Promise.reject(error)
            })
        return instance
    }

    private static setupRequestInterceptor(instance: AxiosInstance): AxiosInstance {
        instance.interceptors.request.use((config) => {
            const accessToken = StorageService.getLocalAccessToken()
            if (accessToken && config.headers)
                config.headers['Authorization'] = `Bearer ${accessToken}`
            return config
        })
        return instance
    }
}
