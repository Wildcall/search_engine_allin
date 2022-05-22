import {ResourceType} from "@/models/ResourceType";

export const resourceStyle = (type: string): { text: string, line: string, bg: string } => {
    if (type === 'CRAWLER')
        return {
            text: 'blue-grey-darken-4',
            line: 'light-blue-lighten-1',
            bg: 'grey-lighten-3'
        }

    if (type === 'INDEXER')
        return {
            text: 'blue-grey-darken-4',
            line: 'orange-lighten-2',
            bg: 'grey-lighten-3'
        }

    if (type === 'SEARCHER')
        return {
            text: 'blue-grey-darken-4',
            line: 'green-lighten-2',
            bg: 'grey-lighten-3'
        }

    if (type === 'TOTAL')
        return {
            text: 'blue-grey-darken-4',
            line: 'indigo-lighten-2',
            bg: 'grey-lighten-3'
        }

    if (type === 'COMPLETE')
        return {
            text: 'blue-grey-darken-4',
            line: 'green-lighten-2',
            bg: 'grey-lighten-3'
        }

    if (type === 'PROGRESSING')
        return {
            text: 'blue-grey-darken-4',
            line: 'amber-lighten-2',
            bg: 'grey-lighten-3'
        }

    if (type === 'INTERRUPT')
        return {
            text: 'blue-grey-darken-4',
            line: 'pink-lighten-2',
            bg: 'grey-lighten-3'
        }

    return {
        text: 'blue-grey-darken-4',
        line: 'blue-grey-darken-4',
        bg: 'grey-lighten-3'
    }
}