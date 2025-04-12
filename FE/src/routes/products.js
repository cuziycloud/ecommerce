import { getProductBySlug } from '../api/fetchProducts';
import { setLoading } from '../store/features/common';
import store from '../store/Store';

export const loadProductBySlug = async ({params}) =>{
    try{
        store.dispatch(setLoading(true));
        const product = await getProductBySlug(params?.slug);
        store.dispatch(setLoading(false));
        return {product};
    }
    catch(err){

    }
}