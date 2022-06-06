import {Api} from "@/api/Api";
import {AxiosResponse} from 'axios'
import {TaskResponse} from "@/models/response/TaskResponse";
import {TaskAddRequest} from "@/models/request/TaskAddRequest";
import {TaskUpdateRequest} from "@/models/request/TaskUpdateRequest";
import {SseParam} from "@/models/SseParam";

export class TaskService {
    private static taskApi = Api.getTask();

    static async findAll(): Promise<AxiosResponse<TaskResponse[]>> {
        console.log("TaskService / findAll")
        return this.taskApi.get<TaskResponse[]>('/task')
    }

    static async add(task: TaskAddRequest): Promise<AxiosResponse<TaskResponse>> {
        console.log("TaskService / post")
        return this.taskApi.post<TaskResponse>('/task', task)
    }

    static async update(task: TaskUpdateRequest): Promise<AxiosResponse<TaskResponse>> {
        console.log("TaskService / update")
        return this.taskApi.put<TaskResponse>('/task', task)
    }

    static async delete(id: number): Promise<AxiosResponse> {
        console.log("TaskService / delete")
        return this.taskApi.delete(`/task/${id}`)
    }

    static async start(id: number): Promise<AxiosResponse<TaskResponse>> {
        console.log("TaskService / start")
        return this.taskApi.get<TaskResponse>(`/task/start/${id}`)
    }

    static async stop(id: number): Promise<AxiosResponse<TaskResponse>> {
        console.log("TaskService / stop")
        return this.taskApi.get<TaskResponse>(`/task/stop/${id}`)
    }
}