import {ResourceType} from "@/models/ResourceType";
import {TaskState} from "@/models/TaskState";
import {SseParam} from "@/models/SseParam";

export class TaskResponse {
    id: number
    siteId: number
    settingId: number
    sendNotification: boolean
    autoContinue: boolean
    eventFreqInMs: number
    statId: number
    type: ResourceType
    createTime: number
    startTime: number
    endTime: number
    taskState: TaskState
    sse: SseParam | null

    constructor(id: number, siteId: number, settingId: number, sendNotification: boolean, autoContinue: boolean, eventFreqInMs: number, statId: number, type: ResourceType, createTime: number, startTime: number, endTime: number, taskState: TaskState, sse: SseParam | null) {
        this.id = id;
        this.siteId = siteId;
        this.settingId = settingId;
        this.sendNotification = sendNotification;
        this.autoContinue = autoContinue;
        this.eventFreqInMs = eventFreqInMs;
        this.statId = statId;
        this.type = type;
        this.createTime = createTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.taskState = taskState;
        this.sse = sse;
    }
}