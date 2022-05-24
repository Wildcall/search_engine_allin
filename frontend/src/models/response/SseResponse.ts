import {Stat} from "@/models/stat/Stat";

export interface SseResponse {
    id: number
    payload: {
        appUserId: number
        siteId: number
        startTime: string
        statistic: Stat
        statusCode: number
        taskId: number
    }
}
