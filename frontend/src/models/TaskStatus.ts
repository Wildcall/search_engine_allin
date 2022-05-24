export const enum TaskStatus {
    CREATE = 'CREATE',
    START = 'START',
    COMPLETE = 'COMPLETE',
    INTERRUPT = 'INTERRUPT',
    ERROR = 'ERROR',
}

export const getTaskStatusById = (id: number) => {
    if (id === 1 || id === 2)
        return TaskStatus.START
    if (id === 3)
        return TaskStatus.COMPLETE
    if (id === 4)
        return TaskStatus.INTERRUPT
    if (id === 5)
        return TaskStatus.ERROR
    return TaskStatus.CREATE
}