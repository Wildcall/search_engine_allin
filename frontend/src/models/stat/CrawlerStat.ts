import {Stat} from "@/models/stat/Stat";

export interface CrawlerStat extends Stat {
    fetchPages: number
    linksCount: number
    savedPages: number
}