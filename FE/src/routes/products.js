import content from '../data/content.json';

export const loadProductById = ({params}) =>{
    // console.log("Loader params:", params);

    const productIdString = params?.productId; 

    if (!productIdString) {
        console.error("Product ID is missing in params");
        return null; 
    }
    const productIdToFind = parseInt(productIdString, 10);

    if (isNaN(productIdToFind)) {
        console.error(`Invalid product ID format: "${productIdString}"`);
        return null; 
    }

    console.log(`Attempting to find product with numeric ID: ${productIdToFind}`);

    const product = content?.products?.find((p) => p?.id === productIdToFind);

    if (product) {
        console.log("Found product:", product);
    } else {
        console.log(`Product with ID ${productIdToFind} not found.`);
    }

    return product || null;
}