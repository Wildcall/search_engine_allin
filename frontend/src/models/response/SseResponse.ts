import {Stat} from "@/models/stat/Stat";

export interface SseResponse {
    id: number
    payload: {
        appUserId: number
        siteId: number
        startTime: "2022-05-15T23:34:38.7139997"
        statistic: Stat | {}
        statusCode: number
        taskId: number
    }
}
