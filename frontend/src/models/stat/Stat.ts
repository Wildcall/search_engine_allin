export abstract class Stat {
    private id: number
    private appUserId: number
    private siteId: number
    private endTime: string
    private startTime: string

    protected constructor(id: number, appUserId: number, siteId: number, endTime: string, startTime: string) {
        this.id = id;
        this.appUserId = appUserId;
        this.siteId = siteId;
        this.endTime = endTime;
        this.startTime = startTime;
    }
}