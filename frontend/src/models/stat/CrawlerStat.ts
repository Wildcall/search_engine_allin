import {Stat} from "@/models/stat/Stat";

export class CrawlerStat extends Stat {
    private _fetchPages: number
    private _linksCount: number
    private _savedPages: number


    constructor(id: number, appUserId: number, siteId: number, endTime: string, startTime: string, fetchPages: number, linksCount: number, savedPages: number) {
        super(id, appUserId, siteId, endTime, startTime);
        this._fetchPages = fetchPages;
        this._linksCount = linksCount;
        this._savedPages = savedPages;
    }

    get fetchPages(): number {
        return this._fetchPages;
    }

    set fetchPages(value: number) {
        this._fetchPages = value;
    }

    get linksCount(): number {
        return this._linksCount;
    }

    set linksCount(value: number) {
        this._linksCount = value;
    }

    get savedPages(): number {
        return this._savedPages;
    }

    set savedPages(value: number) {
        this._savedPages = value;
    }
}