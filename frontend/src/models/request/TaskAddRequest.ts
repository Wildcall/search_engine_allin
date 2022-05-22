export interface TaskAddRequest {
    siteId: number
    settingId: number
    sendNotification: boolean
    autoContinue: boolean
    eventFreqInMs: number
}