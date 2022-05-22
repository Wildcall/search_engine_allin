import {ResourceType} from "@/models/ResourceType";
import {ResourceSetting} from "@/models/setting/ResourceSetting";

export interface SettingResponse {
    id: number
    name: string
    type: ResourceType
    resourceSettingDto: ResourceSetting
}