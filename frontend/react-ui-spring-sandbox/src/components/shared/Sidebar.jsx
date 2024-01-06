'use client';

import {
    IconButton,
    Avatar,
    Box,
    CloseButton,
    Flex,
    HStack,
    VStack,
    Icon,
    useColorModeValue,
    Text,
    Drawer,
    DrawerContent,
    useDisclosure,
    Menu,
    MenuButton,
    MenuDivider,
    MenuItem,
    MenuList,
    Image,
} from '@chakra-ui/react';
import {
    FiHome,
    FiTrendingUp,
    FiCompass,
    FiStar,
    FiSettings,
    FiMenu,
    FiBell,
    FiChevronDown,
} from 'react-icons/fi';

const LinkItems = [
    {name: 'Home', icon: FiHome},
    {name: 'Trending', icon: FiTrendingUp},
    {name: 'Explore', icon: FiCompass},
    {name: 'Favourites', icon: FiStar},
    {name: 'Settings', icon: FiSettings},
];

const SidebarContent = ({onClose, ...rest}) => {
    return (
        <Box
            transition="3s ease"
            bg={useColorModeValue('white', 'gray.900')}
            borderRight="1px"
            borderRightColor={useColorModeValue('gray.200', 'gray.700')}
            w={{base: 'full', md: 60}}
            pos="fixed"
            h="full"
            {...rest}
        >
            <Flex
                h="20"
                alignItems="center"
                mx="8"
                justifyContent="space-between"
                flexDirection="column"
                mb="75"
                mt="2"
            >
                <Text
                    fontSize="2xl"
                    fontFamily="monospace"
                    fontWeight="bold"
                    mb="5"
                >
                    Dashboard
                </Text>
                <Image
                    borderRadius="full"
                    boxSize="75px"
                    src="data:image/png;base64,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"
                    alt="Spring Sandbox"
                />
                <CloseButton
                    display={{base: 'flex', md: 'none'}}
                    onClick={onClose}
                />
            </Flex>
            {LinkItems.map(link => (
                <NavItem key={link.name} icon={link.icon}>
                    {link.name}
                </NavItem>
            ))}
        </Box>
    );
};

const NavItem = ({icon, children, ...rest}) => {
    return (
        <Box
            as="a"
            href="#"
            style={{textDecoration: 'none'}}
            _focus={{boxShadow: 'none'}}
        >
            <Flex
                align="center"
                p="4"
                mx="4"
                borderRadius="lg"
                role="group"
                cursor="pointer"
                _hover={{
                    bg: 'cyan.400',
                    color: 'white',
                }}
                {...rest}
            >
                {icon && (
                    <Icon
                        mr="4"
                        fontSize="16"
                        _groupHover={{
                            color: 'white',
                        }}
                        as={icon}
                    />
                )}
                {children}
            </Flex>
        </Box>
    );
};

const MobileNav = ({onOpen, ...rest}) => {
    return (
        <Flex
            ml={{base: 0, md: 60}}
            px={{base: 4, md: 4}}
            height="20"
            alignItems="center"
            bg={useColorModeValue('white', 'gray.900')}
            borderBottomWidth="1px"
            borderBottomColor={useColorModeValue('gray.200', 'gray.700')}
            justifyContent={{base: 'space-between', md: 'flex-end'}}
            {...rest}
        >
            <IconButton
                display={{base: 'flex', md: 'none'}}
                onClick={onOpen}
                variant="outline"
                aria-label="open menu"
                icon={<FiMenu/>}
            />

            <Text
                display={{base: 'flex', md: 'none'}}
                fontSize="2xl"
                fontFamily="monospace"
                fontWeight="bold"
            >
                Logo
            </Text>

            <HStack spacing={{base: '0', md: '6'}}>
                <IconButton
                    size="lg"
                    variant="ghost"
                    aria-label="open menu"
                    icon={<FiBell/>}
                />
                <Flex alignItems={'center'}>
                    <Menu>
                        <MenuButton
                            py={2}
                            transition="all 0.3s"
                            _focus={{boxShadow: 'none'}}
                        >
                            <HStack>
                                <Avatar
                                    size={'sm'}
                                    src={
                                        'https://images.unsplash.com/photo-1619946794135-5bc917a27793?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&s=b616b2c5b373a80ffc9636ba24f7a4a9'
                                    }
                                />
                                <VStack
                                    display={{base: 'none', md: 'flex'}}
                                    alignItems="flex-start"
                                    spacing="1px"
                                    ml="2"
                                >
                                    <Text fontSize="sm">Justina Clark</Text>
                                    <Text fontSize="xs" color="gray.600">
                                        Admin
                                    </Text>
                                </VStack>
                                <Box display={{base: 'none', md: 'flex'}}>
                                    <FiChevronDown/>
                                </Box>
                            </HStack>
                        </MenuButton>
                        <MenuList
                            bg={useColorModeValue('white', 'gray.900')}
                            borderColor={useColorModeValue(
                                'gray.200',
                                'gray.700'
                            )}
                        >
                            <MenuItem>Profile</MenuItem>
                            <MenuItem>Settings</MenuItem>
                            <MenuItem>Billing</MenuItem>
                            <MenuDivider/>
                            <MenuItem>Sign out</MenuItem>
                        </MenuList>
                    </Menu>
                </Flex>
            </HStack>
        </Flex>
    );
};

const SidebarWithHeader = ({children}) => {
    const {isOpen, onOpen, onClose} = useDisclosure();

    return (
        <Box minH="100vh" bg={useColorModeValue('gray.100', 'gray.900')}>
            <SidebarContent
                onClose={() => onClose}
                display={{base: 'none', md: 'block'}}
            />
            <Drawer
                isOpen={isOpen}
                placement="left"
                onClose={onClose}
                returnFocusOnClose={false}
                onOverlayClick={onClose}
                size="full"
            >
                <DrawerContent>
                    <SidebarContent onClose={onClose}/>
                </DrawerContent>
            </Drawer>
            {/* mobilenav */}
            <MobileNav onOpen={onOpen}/>
            <Box ml={{base: 0, md: 60}} p="4">
                {children}
            </Box>
        </Box>
    );
};

export default SidebarWithHeader;
