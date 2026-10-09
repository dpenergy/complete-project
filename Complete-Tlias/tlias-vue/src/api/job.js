import request from "@/utils/request";

const queryJobListApi = () => request.get("/jobs")

export {queryJobListApi}