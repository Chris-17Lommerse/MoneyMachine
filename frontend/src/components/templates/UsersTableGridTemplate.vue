<script setup>
import UserRecordMolecule from '../molecules/users/tablerows/UserRecordMolecule.vue';
import UserTableHeader from '../molecules/users/tablerows/UserTableHeader.vue';

const props = defineProps({
    users: {
        type: Array,
        required: true,
        validator: (value) => {
            return value.every(user =>
                typeof user.userId === 'number' &&
                typeof user.firstName === 'string' &&
                typeof user.lastName === 'string' &&
                typeof user.email === 'string' &&
                typeof user.bsnNumber === 'string' &&
                typeof user.phoneNumber === 'string' &&
                typeof user.role === 'string' &&
                typeof user.isActive === 'boolean' &&
                typeof user.isApproved === 'boolean'
            )
        }
    }
})
</script>

<template>
    <table>
        <thead>
            <UserTableHeader />
        </thead>
        <tbody>
            <UserRecordMolecule v-for="user in users" :key="user.userId" :user="user" />
        </tbody>
    </table>
</template>