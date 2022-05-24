import {defineStore} from "pinia";
import {SiteResponse} from "@/models/response/SiteResponse";
import {SiteService} from "@/services/SiteService";
import {useErrorStore} from "@/store/ErrorStore";
import {Site} from "@/models/Site";
import {useTaskStore} from "@/store/TaskStore";
import {useSettingStore} from "@/store/SettingStore";
import {SiteStatus} from "@/models/SiteStatus";
import {TaskStatus} from "@/models/TaskStatus";

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

        getSite(state: SiteState): (id: number) => SiteResponse | null {
            return (id: number) => {
                const index = state.sites.findIndex(site => site.id === id)
                if (index === -1) return null
                return state.sites[index]
            }
        },

        getProcessingSitesCount(state: SiteState): number {
            const taskStore = useTaskStore()
            return state.sites
                .map(site => {
                    const lng = taskStore.getSiteTask(site.id).filter(task => {
                        return task.taskState === TaskStatus.START
                    }).length
                    return lng === 0 ? 0 : 1
                })
                .reduce((prv: number, cur: number) => prv + cur, 0)
        },

        getCountSitesWithAllCompleteTasks(state: SiteState): number {
            const taskStore = useTaskStore()
            return state.sites
                .map(site => {
                    const lng = taskStore.getSiteTask(site.id).filter(task => {
                        return task.taskState === TaskStatus.COMPLETE
                    }).length
                    return lng === 3 ? 1 : 0
                })
                .reduce((prv: number, cur: number) => prv + cur, 0)

        },

        getCountSitesWithInterruptTasks(state: SiteState): number {
            const taskStore = useTaskStore()
            return state.sites
                .map(site => {
                    const lng = taskStore.getSiteTask(site.id).filter(task => {
                        return task.taskState === TaskStatus.INTERRUPT
                    }).length
                    return lng === 0 ? 0 : 1
                })
                .reduce((prv: number, cur: number) => prv + cur, 0)

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
                    taskStore.reloadCache()
                    settingStore.reloadCache()
                })
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        }
    }
})

