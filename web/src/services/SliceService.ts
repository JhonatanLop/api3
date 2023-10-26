import axios, { AxiosResponse } from 'axios';
import { SliceSchema } from '../schemas/Slice';

const API_URL = 'http://127.0.0.1:8080/slices';


function serializeSlice (slice: any): SliceSchema {
    return {
        appointmentId: slice.appointmentId ? slice.appointmentId : "N/A",
        payRateRuleId: slice.payRateRuleId ? slice.payRateRuleId : "N/A",
        start: slice.start ? slice.start : "N/A",
        end: slice.end ? slice.end : "N/A",
    }
}

async function mapResponse (response: AxiosResponse) {
    return response.data.map((item) => serializeSlice(item));
}

export async function getSlices (): Promise<SliceSchema[]> {
    const response = await axios.get(API_URL, {});
    return await mapResponse(response);
}
