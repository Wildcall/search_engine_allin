import {SseParam} from "@/models/SseParam";
import {ResourceType} from "@/models/ResourceType";
import {Api} from "@/api/Api";
import {SseResponse} from "@/models/response/SseResponse";
import {useSseStore} from "@/store/SseStore";

export class SseService {

    static subscribe(sseParam: SseParam, type: ResourceType) {
        const eventSource = Api.getSse(sseParam, type);
        const sseStore = useSseStore()
        console.log("Open connection")
        return eventSource.addEventListener('message', (e) => {
            try {
                const response: SseResponse = JSON.parse(e.data) as SseResponse
                sseStore.add(response)
                console.log(response)
                if (response.payload.statusCode !== 1) {
                    console.log("Close connection")
                    eventSource.close()
                }
            } catch (e) {
                console.error(e)
                console.log("Close connection")
                eventSource.close()
            }
        })
    }
}