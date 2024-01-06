'use client'

import {
    Heading,
    Avatar,
    Box,
    Center,
    Text,
    Stack,
    Button,
    Link,
    Badge,
    useColorModeValue, Tag,
} from '@chakra-ui/react'

export default function Card({id, name, age, email, gender}) {
    return (
        <Center py={6}>
            <Box
                maxW={'500px'}
                w={'full'}
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
                        _focus={{
                            bg: 'gray.200',
                        }}>
                        Message
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
                            bg: 'blue.500',
                        }}
                        _focus={{
                            bg: 'blue.500',
                        }}>
                        Follow
                    </Button>
                </Stack>
            </Box>
        </Center>
    )
}