import {ResourceSetting} from "@/models/setting/ResourceSetting";
import {ResourceType} from "@/models/ResourceType";

export class IndexerSetting extends ResourceSetting {
    private name: string
    private selectorWeight: Map<string, number>
    private parallelism: number

    constructor(name: string,
                selectorWeight: Map<string, number>,
                parallelism: number) {
        super();
        this.name = name
        this.selectorWeight = selectorWeight;
        this.parallelism = parallelism;
    }

    getType(): ResourceType {
        return ResourceType.INDEXER;
    }
}