import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent,
    DrawerFooter,
    DrawerHeader, DrawerOverlay,
    Input, useDisclosure
} from "@chakra-ui/react";
import React from "react";

const AddIcon = () => "+";

const DrawerForm = () => {
    const {isOpen, onOpen, onClose} = useDisclosure()
    const btnRef = React.useRef()
    return (
        <div>

            <Button
                leftIcon={<AddIcon/>}
                colorScheme={"teal"}
                onClick={onOpen}
            >
                Create customer
            </Button>
            <Drawer
                isOpen={isOpen}
                placement='right'
                onClose={onClose}
                finalFocusRef={btnRef}
                size={"xl"}
            >
                <DrawerOverlay/>
                <DrawerContent>
                    <DrawerCloseButton/>
                    <DrawerHeader>Create your account</DrawerHeader>

                    <DrawerBody>
                        <Input placeholder='Type here...'/>
                    </DrawerBody>

                    <DrawerFooter>
                        <Button variant='outline' mr={3} onClick={onClose}>
                            Cancel
                        </Button>
                        <Button colorScheme='blue'>Save</Button>
                    </DrawerFooter>
                </DrawerContent>
            </Drawer>
        </div>
    )
}

export {DrawerForm};