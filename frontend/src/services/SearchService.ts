import {AxiosResponse} from "axios";
import {SearchResponse} from "@/models/response/SearchResponse";
import {Api} from "@/api/Api";

export class SearchService {
    static async search(siteId: number, query: string): Promise<AxiosResponse<SearchResponse[]>> {
        console.log('SearchService / search')
        return Api.getSearch().get<SearchResponse[]>(`?siteId=${siteId}&query=${query}`)
    }
}