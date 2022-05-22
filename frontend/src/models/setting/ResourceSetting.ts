import {ResourceType} from "@/models/ResourceType";

export abstract class ResourceSetting {
    getType(): ResourceType | null {
        return null
    }
}
