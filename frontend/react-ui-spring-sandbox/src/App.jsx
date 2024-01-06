import { Wrap, WrapItem, Spinner, Text } from '@chakra-ui/react';
import SidebarWithHeader from './components/shared/Sidebar.jsx';
import { useEffect, useState } from 'react';
import { getCustomers } from './services/client.js';
import Card from './components/Card.jsx';
import {DrawerForm} from "./components/DrawerForm.jsx";

const App = () => {
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);
        getCustomers()
            .then(res => {
                setCustomers(res.data);
            })
            .catch(err => console.log(err))
            .finally(() => setLoading(false));
    }, []);
    if (loading) {
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness="4px"
                    speed="0.65s"
                    emptyColor="gray.200"
                    color="blue.500"
                    size="xl"
                ></Spinner>
            </SidebarWithHeader>
        );
    }

    if (customers.length <= 0) {
        return (
            <SidebarWithHeader>
                <Text>No customers</Text>
            </SidebarWithHeader>
        );
    }
    return (
        <SidebarWithHeader>
            <DrawerForm/>
            <Wrap justify={'center'} spacing={'30px'}>
                {customers.map((customer, index) => (
                    <WrapItem key={index}>
                        <Card {...customer}></Card>
                    </WrapItem>
                ))}
            </Wrap>
        </SidebarWithHeader>
    );
};

export default App;
