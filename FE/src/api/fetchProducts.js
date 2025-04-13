import axios from "axios";
import { API_BASE_URL, API_URLS } from "./constant"


export const getAllProducts = async (id, typeId) => {
    let url = API_BASE_URL + API_URLS.GET_PRODUCTS + `?categoryId=${id}`;
    if (typeId) {
        url = url + `&typeId=${typeId}`;
    }
    console.log('Requesting URL for similar products:', url); // Thêm log URL

    try {
        const result = await axios(url, {
            method: "GET"
        });
        if (Array.isArray(result?.data)) {
            return result.data;
        } else {
            console.warn("API did not return an array in data property:", result?.data)
            return []; 
        }
    }
    catch (err) {
        console.error("Error fetching getAllProducts:", err);
        return [];
    }
}

export const getProductBySlug = async (slug)=>{
    const url = API_BASE_URL + API_URLS.GET_PRODUCTS + `?slug=${slug}`;
    try{
        const result = await axios(url,{
            method:"GET",
        });
        return result?.data?.[0];
    }
    catch(err){
        console.error(err);
    }
}