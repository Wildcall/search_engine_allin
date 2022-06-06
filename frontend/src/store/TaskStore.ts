import {defineStore} from "pinia";
import {useErrorStore} from "@/store/ErrorStore";
import {TaskResponse} from "@/models/response/TaskResponse";
import {TaskService} from "@/services/TaskService";
import {TaskAddRequest} from "@/models/request/TaskAddRequest";
import {TaskUpdateRequest} from "@/models/request/TaskUpdateRequest";

export interface TaskState {
    loading: boolean
    tasks: TaskResponse[]
}

export const useTaskStore = defineStore({
    id: "task",

    state: () => ({
        loading: false,
        tasks: []
    } as TaskState),

    getters: {
        getLoading(state: TaskState): boolean {
            return state.loading
        },

        getTasks(state: TaskState): TaskResponse[] {
            return state.tasks
        },

        getSiteTask(state: TaskState): (siteId: number) => TaskResponse[] {
            return (siteId: number) => {
                return state.tasks.filter(obj => obj.siteId === siteId)
            }
        }
    },

    actions: {
        async findAll() {
            if (this.tasks[0]) {
                console.log('TaskStore / findAll / fromCache')
                return
            }
            console.log('TaskStore / findAll')
            const errorStore = useErrorStore()
            this.loading = true
            TaskService.findAll()
                .then((response) => this.tasks = response?.data)
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },

        async add(task: TaskAddRequest) {
            console.log('TaskStore / add')
            const errorStore = useErrorStore()
            this.loading = true
            TaskService.add(task)
                .then((response) => {
                    this.tasks.push(response.data)
                })
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },

        async update(task: TaskUpdateRequest) {
            console.log('TaskStore / update')
            const errorStore = useErrorStore()
            this.loading = true
            TaskService.update(task)
                .then((response) => {
                    this.tasks.push(response.data)
                })
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },

        async delete(id: number) {
            console.log('TaskStore / delete')
            const errorStore = useErrorStore()
            this.loading = true
            TaskService.delete(id)
                .then(() => {
                    this.tasks = this.tasks.filter(obj => obj.id !== id)
                })
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },

        async start(id: number) {
            console.log('TaskStore / start')
            const errorStore = useErrorStore()
            this.loading = true
            TaskService.start(id)
                .then((response) => {
                    if (response) {
                        const index = this.tasks.findIndex(obj => obj.id === id)
                        if (index > -1)
                            this.tasks[index] = response.data
                        // const task = this.tasks.find(obj => obj.id === id) as TaskResponse
                        // task.sse = r.data
                        // task.startTime = null
                        // task.endTime = null
                        // SseService.subscribe(r.data, task.type, task)
                    }
                })
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },

        async stop(id: number) {
            console.log('TaskStore / stop')
            const errorStore = useErrorStore()
            this.loading = true
            TaskService.stop(id)
                .then((response) => {
                    if (response) {
                        const index = this.tasks.findIndex(obj => obj.id === id)
                        if (index > -1)
                            this.tasks[index] = response.data
                        // const task = this.tasks.find(obj => obj.id === id) as TaskResponse
                        // task.sse = null
                    }
                })
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },

        async reloadCache() {
            console.log('TaskStore / reloadCache')
            this.loading = false
            this.tasks = []
            await this.findAll()
        }
    }
})

