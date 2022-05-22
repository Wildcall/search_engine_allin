export interface ApiError {
    message: string
    status: string
    subErrors: [SubError]
    timestamp: string
}

export interface SubError {
    object: string
    field: string
    rejectedValue: string
    message: string
}