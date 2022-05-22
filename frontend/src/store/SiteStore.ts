import {defineStore} from "pinia";
import {SiteResponse} from "@/models/response/SiteResponse";
import {SiteService} from "@/services/SiteService";
import {useErrorStore} from "@/store/ErrorStore";
import {Site} from "@/models/Site";
import {useTaskStore} from "@/store/TaskStore";
import {useSettingStore} from "@/store/SettingStore";
import {SiteStatus} from "@/models/SiteStatus";

export interface SiteState {
    loading: boolean
    sites: SiteResponse[]
}

export const useSiteStore = defineStore({
    id: "site",

    state: () => ({
        loading: false,
        sites: []
    } as SiteState),

    getters: {
        getLoading(state: SiteState): boolean {
            return state.loading
        },

        getSites(state: SiteState): SiteResponse[] {
            return state.sites
        },

        getProcessingSitesCount(state: SiteState): number {
            return state.sites.filter(obj => obj.status === SiteStatus.PROCESSING).length
        },

        getCountSitesWithAllCompleteTasks(state: SiteState): number {
            state.sites.map(obj => obj.id)
            return 0
        }
    },

    actions: {
        async findAll() {
            if (this.sites[0]) {
                console.log('SiteStore / findAll / fromCache')
                return
            }
            console.log('SiteStore / findAll')
            const errorStore = useErrorStore()
            this.loading = true
            SiteService.findAll()
                .then((response) => this.sites = response?.data)
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },

        async add(site: Site) {
            console.log('SiteStore / add')
            const errorStore = useErrorStore()
            this.loading = true
            SiteService.add(site)
                .then((response) => {
                    this.sites.push(response.data)
                })
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },

        async delete(id: number) {
            console.log('SiteStore / delete')
            const errorStore = useErrorStore()
            const taskStore = useTaskStore()
            const settingStore = useSettingStore()
            this.loading = true
            SiteService.delete(id)
                .then(() => {
                    this.sites = this.sites.filter(obj => obj.id !== id)
                    taskStore.clearCache()
                    settingStore.clearCache()
                })
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        }
    }
})

