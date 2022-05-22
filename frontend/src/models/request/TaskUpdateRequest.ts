export interface TaskUpdateRequest {
    id: number
    sendNotification: boolean
    autoContinue: boolean
    eventFreqInMs: number
}