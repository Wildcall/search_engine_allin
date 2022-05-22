import {Api} from "@/api/Api";
import {AxiosResponse} from 'axios'
import {Site} from "@/models/Site";
import {SiteResponse} from "@/models/response/SiteResponse";

export class SiteService {
    private static taskApi = Api.getTask();

    static async add(site: Site): Promise<AxiosResponse<SiteResponse>> {
        console.log("SiteService / add")
        return this.taskApi.post<SiteResponse>('/site', site)
    }

    static async findAll(): Promise<AxiosResponse<SiteResponse[]>> {
        console.log("SiteService / findAll")
        return this.taskApi.get<SiteResponse[]>('/site')
    }

    static async delete(id: number): Promise<AxiosResponse> {
        console.log("SiteService / delete")
        return this.taskApi.delete(`/site/${id}`)
    }
}