import {Stat} from "@/models/stat/Stat";

export interface IndexerStat extends Stat {
   createdIndexes: number
   parsedPages: number
   savedLemmas: number
}