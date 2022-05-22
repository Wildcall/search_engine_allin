import {SiteStatus} from "@/models/SiteStatus";

export interface SiteResponse {
    id: number
    name: string
    path: string
    lastStatusTime: string
    status: SiteStatus
}