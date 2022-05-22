import {defineStore} from "pinia";
import {useErrorStore} from "@/store/ErrorStore";
import {useTaskStore} from "@/store/TaskStore";
import {SettingResponse} from "@/models/response/SettingResponse";
import {SettingService} from "@/services/SettingService";
import {ResourceType} from "@/models/ResourceType";
import {ResourceSetting} from "@/models/setting/ResourceSetting";

export interface SettingState {
    loading: boolean
    settings: SettingResponse[]
}

export const useSettingStore = defineStore({
    id: "setting",

    state: () => ({
        loading: false,
        settings: []
    } as SettingState),

    getters: {
        getLoading(state: SettingState): boolean {
            return state.loading
        },

        getSettings(state: SettingState): SettingResponse[] {
            return state.settings
        },

        countSettingsOfType(state: SettingState): (type: ResourceType) => number {
            return (type: ResourceType) => {
                return state.settings.filter(obj => obj.type === type).length
            }
        },

        getSettingsByType(state: SettingState): (type: ResourceType) => SettingResponse[] {
            return (type: ResourceType) => {
                return state.settings.filter(obj => obj.type === type)
            }
        }
    },

    actions: {
        async findAll() {
            if (this.settings[0]) {
                console.log('SettingStore / findAll / fromCache')
                return
            }
            console.log('SettingStore / findAll')
            const errorStore = useErrorStore()
            this.loading = true
            SettingService.findAll()
                .then((response) => this.settings = response?.data)
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },

        async add(setting: ResourceSetting, type: ResourceType) {
            console.log('SettingStore / add')
            const errorStore = useErrorStore()
            this.loading = true
            SettingService.add(setting, type)
                .then((response) => {
                    this.settings.push(response.data)
                })
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },

        async delete(id: number) {
            console.log('SettingStore / delete')
            const errorStore = useErrorStore()
            const taskStore = useTaskStore()
            this.loading = true
            SettingService.delete(id)
                .then(() => {
                    this.settings = this.settings.filter(obj => obj.id !== id)
                    taskStore.clearCache()
                })
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },

        clearCache() {
            console.log('TaskStore / clearCache')
            this.loading = false
            this.settings = []
        }
    }
})

