'use client'

import {
    Heading,
    Avatar,
    Box,
    Center,
    Text,
    Stack,
    Button,
    Badge,
    useColorModeValue,
    Tag,
    useDisclosure,
    Modal,
    ModalOverlay,
    ModalContent,
    ModalHeader,
    ModalCloseButton,
    ModalBody,
    ModalFooter, Drawer, DrawerOverlay, DrawerContent, DrawerCloseButton, DrawerHeader, DrawerBody, DrawerFooter,
} from '@chakra-ui/react'
import {errorNotification, successNotification} from "../services/notification.js";
import {deleteCustomerById} from "../services/client.js";
import React from "react";
import EditCustomerForm from "./EditCustomerForm.jsx";

export default function Card({id, name, age, email, gender, fetchCustomers}) {
    const {isOpen: isDeleteOpen, onOpen: onDeleteOpen, onClose: onDeleteClose} = useDisclosure();
    const {isOpen: isEditOpen, onOpen: onEditOpen, onClose: onEditClose} = useDisclosure();
    const btnRef = React.useRef();
    return (
        <Center py={6}>
            <Box
                maxW={'500px'}
                w={'50vh'}
                h={'full'}
                bg={useColorModeValue('white', 'gray.900')}
                boxShadow={'2xl'}
                rounded={'lg'}
                p={10}
                textAlign={'center'}>
                <Avatar
                    size={'xl'}
                    src={
                        `https://randomuser.me/api/portraits/med/${gender === 'MALE' ? 'men' : 'women'}/${id}.jpg`
                    }
                    mb={4}
                    pos={'relative'}
                    _after={{
                        content: '""',
                        w: 4,
                        h: 4,
                        bg: 'green.300',
                        border: '2px solid white',
                        rounded: 'full',
                        pos: 'absolute',
                        bottom: 0,
                        right: 3,
                    }}
                />
                <Heading fontSize={'2xl'} fontFamily={'body'}>
                    {name}
                </Heading>
                <Text fontWeight={600} color={'gray.500'} mb={4}>
                    {email}
                </Text>
                <Text fontWeight={600} color={'gray.500'} mb={4}>
                    <Tag>Age {age}</Tag>
                </Text>

                <Stack align={'center'} justify={'center'} direction={'row'} mt={6}>
                    <Badge
                        px={2}
                        py={1}
                        bg={useColorModeValue('gray.50', 'gray.800')}
                        fontWeight={'400'}>
                        {gender}
                    </Badge>
                </Stack>

                <Stack mt={8} direction={'row'} spacing={4}>
                    <Button
                        flex={1}
                        fontSize={'sm'}
                        rounded={'full'}
                        bg={'red.400'}
                        color={'white'}
                        boxShadow={
                            '0px 1px 25px -5px rgb(66 153 225 / 48%), 0 10px 10px -5px rgb(66 153 225 / 43%)'
                        }
                        _hover={{
                            transform: 'translateY(-2px)',
                            boxShadow: 'lg'
                        }}
                        _focus={{
                            bg: 'grey.500',
                        }}
                        onClick={onDeleteOpen}
                    >
                        Delete
                    </Button>
                    <Button
                        flex={1}
                        fontSize={'sm'}
                        rounded={'full'}
                        bg={'blue.400'}
                        color={'white'}
                        boxShadow={
                            '0px 1px 25px -5px rgb(66 153 225 / 48%), 0 10px 10px -5px rgb(66 153 225 / 43%)'
                        }
                        _hover={{
                            transform: 'translateY(-2px)',
                            boxShadow: 'lg'
                        }}
                        _focus={{
                            bg: 'grey.500',
                        }}
                        onClick={onEditOpen}
                    >
                        Edit
                    </Button>

                    <Modal isOpen={isDeleteOpen} onClose={onDeleteClose}>
                        <ModalOverlay/>
                        <ModalContent>
                            <ModalHeader>Confirm action</ModalHeader>
                            <ModalCloseButton/>
                            <ModalBody>
                                Are you sure that you want to delete customer {name}
                            </ModalBody>

                            <ModalFooter>
                                <Button colorScheme='blue' mr={3} onClick={onDeleteClose}>
                                    Cancel
                                </Button>
                                <Button
                                    variant='ghost'
                                    onClick={
                                        () => deleteCustomerById(id)
                                            .then(res => successNotification(
                                                `Success!`,
                                                `Customer ${name} successfully deleted!`
                                            ))
                                            .catch(err => errorNotification(
                                                `Server returned an error code ${err.response.status}`,
                                                `Error: ${err.response.data.message}`
                                            ))
                                            .finally(() => fetchCustomers())
                                    }
                                >
                                    Delete
                                </Button>
                            </ModalFooter>
                        </ModalContent>
                    </Modal>
                    <Drawer
                        isOpen={isEditOpen}
                        placement='right'
                        onClose={onEditClose}
                        finalFocusRef={btnRef}
                        size={"xl"}
                    >
                        <DrawerOverlay/>
                        <DrawerContent>
                            <DrawerCloseButton/>
                            <DrawerHeader>Edit customer</DrawerHeader>

                            <DrawerBody>
                                <EditCustomerForm
                                    id={id}
                                    name={name}
                                    age={age}
                                    email={email}
                                    gender={gender}
                                    fetchCustomers={fetchCustomers}
                                />
                            </DrawerBody>

                            <DrawerFooter>
                                <Button variant='outline' mr={3} onClick={onEditClose}>
                                    Cancel
                                </Button>
                            </DrawerFooter>
                        </DrawerContent>
                    </Drawer>
                </Stack>
            </Box>
        </Center>
    )
}