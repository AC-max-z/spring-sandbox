import client from 'axios';

const CUSTOMER_API_ENDPOINT = '/api/v1/customer';

export const getCustomers = async () => {
    try {
        console.log(import.meta.env);
        return await client.get(
            `${import.meta.env.VITE_API_BASE_URL}${CUSTOMER_API_ENDPOINT}/all`
        );
    } catch (err) {
        console.log(err);
        throw err;
    }
};
