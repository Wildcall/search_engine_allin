import {Stat} from "@/models/stat/Stat";

export class IndexerStat extends Stat {
    private _createdIndexes: number
    private _parsedPages: number
    private _savedLemmas: number


    constructor(id: number, appUserId: number, siteId: number, endTime: string, startTime: string, createdIndexes: number, parsedPages: number, savedLemmas: number) {
        super(id, appUserId, siteId, endTime, startTime);
        this._createdIndexes = createdIndexes;
        this._parsedPages = parsedPages;
        this._savedLemmas = savedLemmas;
    }


    get createdIndexes(): number {
        return this._createdIndexes;
    }

    set createdIndexes(value: number) {
        this._createdIndexes = value;
    }

    get parsedPages(): number {
        return this._parsedPages;
    }

    set parsedPages(value: number) {
        this._parsedPages = value;
    }

    get savedLemmas(): number {
        return this._savedLemmas;
    }

    set savedLemmas(value: number) {
        this._savedLemmas = value;
    }
}