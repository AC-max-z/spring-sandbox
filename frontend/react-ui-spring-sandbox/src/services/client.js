import client from 'axios';

const CUSTOMER_API_ENDPOINT = '/api/v1/customer';

export const getCustomers = async () => {
    try {
        return await client.get(
            `${import.meta.env.VITE_API_BASE_URL}${CUSTOMER_API_ENDPOINT}/all`
        );
    } catch (err) {
        throw err;
    }
};

export const saveCustomer = async (customer) => {
    try {
        return await client.post(
            `${import.meta.env.VITE_API_BASE_URL}${CUSTOMER_API_ENDPOINT}`,
            customer
        )
    } catch (e) {
        throw e;
    }
}

export const deleteCustomerById = async (id) => {
    try {
        return await client.delete(`${import.meta.env.VITE_API_BASE_URL}${CUSTOMER_API_ENDPOINT}/${id}`)
    } catch (err) {
        throw err;
    }
}

export const updateCustomer = async (customer) => {
    try {
        return await client.put(
            `${import.meta.env.VITE_API_BASE_URL}${CUSTOMER_API_ENDPOINT}/${customer.id}`,
            customer);
    } catch (err) {
        throw err;
    }
}
