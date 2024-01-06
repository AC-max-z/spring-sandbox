import {Wrap, WrapItem, Spinner, Text} from '@chakra-ui/react';
import SidebarWithHeader from './components/shared/Sidebar.jsx';
import {useEffect, useState} from 'react';
import {getCustomers} from './services/client.js';
import Card from './components/Card.jsx';
import {DrawerForm} from "./components/DrawerForm.jsx";
import {errorNotification} from "./services/notification.js";

const App = () => {
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);

    const fetchCustomers = () => {
        setLoading(true);
        getCustomers()
            .then(res => {
                setCustomers(res.data);
            })
            .catch(err =>
                errorNotification(
                    `Server returned an error code ${err.response.status}`,
                    `Error: ${err.response.data.message}`
                )
            )
            .finally(() => setLoading(false));
    }

    useEffect(() => {
        fetchCustomers();
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
                <DrawerForm/>
                <Text>No customers</Text>
            </SidebarWithHeader>
        );
    }
    return (
        <SidebarWithHeader>
            <DrawerForm
                fetchCustomers={fetchCustomers}
            />
            <Wrap justify={'center'} spacing={'30px'}>
                {customers.map((customer, index) => (
                    <WrapItem key={index}>
                        <Card
                            id={customer.id}
                            name={customer.name}
                            age={customer.age}
                            email={customer.email}
                            gender={customer.gender}
                            fetchCustomers={fetchCustomers}
                        />
                    </WrapItem>
                ))}
            </Wrap>
        </SidebarWithHeader>
    );
};

export default App;
