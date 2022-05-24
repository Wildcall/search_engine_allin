import {SseParam} from "@/models/SseParam";
import {ResourceType} from "@/models/ResourceType";
import {Api} from "@/api/Api";
import {SseResponse} from "@/models/response/SseResponse";
import {useSseStore} from "@/store/SseStore";
import {TaskResponse} from "@/models/response/TaskResponse";
import {getTaskStatusById} from "@/models/TaskStatus";

export class SseService {

    static subscribe(sseParam: SseParam, type: ResourceType, task: TaskResponse) {
        const eventSource = Api.getSse(sseParam, type);
        const sseStore = useSseStore()
        console.log("Open connection")
        return eventSource.addEventListener('message', (e) => {
            try {
                const response = JSON.parse(e.data) as SseResponse
                sseStore.add(response)
                if (!task.startTime) {
                    const date = new Date(Date.parse(response.payload.startTime));
                    task.startTime = this.getStringDate(date)
                }
                task.taskState = getTaskStatusById(response.payload.statusCode)
                if (response.payload.statusCode !== 1 && response.payload.statusCode !== 2) {
                    console.log("Close connection")
                    const date = new Date(Date.parse(response.payload?.statistic?.endTime));
                    task.endTime = this.getStringDate(date)
                    sseStore.clear(sseParam.siteId)
                    eventSource.close()
                }
            } catch (e) {
                console.error(e)
                console.log("Close connection")
                eventSource.close()
            }
        })
    }

    static getStringDate(date: Date): string {
        const year = date.getFullYear()
        const month = ("0" + (date.getMonth() + 1)).slice(-2)
        const day = ("0" + date.getDate()).slice(-2)
        const hour = ("0" + date.getHours()).slice(-2)
        const min = ("0" + date.getMinutes()).slice(-2)
        return `${year}-${month}-${day} ${hour}:${min}`
    }
}