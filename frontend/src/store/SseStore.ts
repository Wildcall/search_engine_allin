import {defineStore} from "pinia";
import {SseResponse} from "@/models/response/SseResponse";

export interface SseState {
    connection: boolean
    data: {
        siteId: number
        sseData: SseResponse[]
    }[]
}

export const useSseStore = defineStore({
    id: "sse",

    state: () => ({
        connection: false,
        data: []
    } as SseState),

    getters: {
        getConnection(state: SseState): boolean {
            return state.connection
        },

        getSseDataForSite(state: SseState): (siteId: number) => SseResponse[] | undefined {
            return (siteId: number) => {
                return state.data?.find(obj => obj.siteId === siteId)?.sseData
            }
        }
    },

    actions: {
        add(sseR: SseResponse) {
            let index = this.data.findIndex(obj => obj.siteId === sseR.payload?.siteId)
            if (index === -1) {
                this.data.push({siteId: sseR.payload?.siteId, sseData: []})
                index = 0
            }
            this.data[index].sseData.push(sseR)
            this.connection = true
        },

        clear(siteId: number) {
            this.data = this.data.filter(obj => obj.siteId !== siteId)
            this.connection = false
        }
    }


})