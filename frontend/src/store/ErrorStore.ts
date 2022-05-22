import {ApiError, SubError} from "@/models/ApiError";
import {defineStore} from "pinia";
import {AxiosError} from 'axios'

export interface ErrorState {
    error: boolean
    apiError: ApiError
}

export const useErrorStore = defineStore({
    id: 'error',

    state: () => ({
        error: false,
        apiError: {} as ApiError,
    } as ErrorState),

    getters: {
        hasError(state: ErrorState): boolean {
            return state.error
        },

        getMessage(state: ErrorState): string {
            if (state?.apiError?.message) return state.apiError.message
            return "Unexpected error"
        },

        getStatus(state: ErrorState): string {
            return state.apiError.status
        },

        getSubErrors(state: ErrorState): [SubError] {
            return state.apiError.subErrors
        }
    },

    actions: {
        save(axiosError: AxiosError) {
            console.log("ErrorStore / save")
            this.$patch({
                apiError: axiosError.response?.data as ApiError,
                error: true
            })
        },

        remove() {
            console.log("ErrorStore / remove")
            this.$patch({
                apiError: {} as ApiError,
                error: false
            })
        }
    }
})