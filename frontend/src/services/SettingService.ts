import {Api} from "@/api/Api";
import {AxiosResponse} from 'axios'
import {SettingResponse} from "@/models/response/SettingResponse";
import {ResourceType} from "@/models/ResourceType";
import {ResourceSetting} from "@/models/setting/ResourceSetting";

export class SettingService {
    private static taskApi = Api.getTask();

    static async findAll(): Promise<AxiosResponse<SettingResponse[]>> {
        console.log("SettingService / findAll")
        return this.taskApi.get<SettingResponse[]>('/setting')
    }

    static async add(setting: ResourceSetting, type: ResourceType): Promise<AxiosResponse<SettingResponse>> {
        console.log("SettingService / add")
        return this.taskApi.post<SettingResponse>(`/setting/${type.toLowerCase()}`, setting)
    }

    static async delete(id: number): Promise<AxiosResponse> {
        console.log("SettingService / delete")
        return this.taskApi.delete(`/setting/${id}`)
    }
}