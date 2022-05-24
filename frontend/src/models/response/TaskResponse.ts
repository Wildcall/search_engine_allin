import {ResourceType} from "@/models/ResourceType";
import {TaskStatus} from "@/models/TaskStatus";
import {SseParam} from "@/models/SseParam";

export interface TaskResponse {
    id: number
    siteId: number
    settingId: number
    sendNotification: boolean
    autoContinue: boolean
    eventFreqInMs: number
    statId: number
    type: ResourceType
    createTime: string | null
    startTime: string | null
    endTime: string | null
    taskState: TaskStatus
    sse: SseParam | null
}