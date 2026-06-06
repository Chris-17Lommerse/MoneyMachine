<script setup>
    import UserRecordMolecule from '@/components/molecules/users/tablerows/UserRecordMolecule.vue';
    import UserTableHeader from '@/components/molecules/users/tablerows/UserTableHeader.vue';

    const props = defineProps({
        items: {
            type: Array,
            required: true,
            validator: (value) => {
                return value.every(user =>
                    typeof user.userId === 'number' &&
                    typeof user.firstName === 'string' &&
                    typeof user.lastName === 'string' &&
                    typeof user.email === 'string' &&
                    typeof user.bsn === 'string' &&
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
    <table class="table table-striped">
        <thead>
            <UserTableHeader />
        </thead>
        <tbody>
            <UserRecordMolecule v-for="user in items" :key="user.userId" :user="user" />
        </tbody>
    </table>
</template>