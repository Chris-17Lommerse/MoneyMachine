<script setup>
import ActionsTableHeaderAtom from "@/atoms/users/textelements/tableheaders/ActionsTableHeaderAtom.vue";
import BSNNumberTableHeaderAtom from "@/atoms/users/textelements/tableheaders/BSNNumberTableHeaderAtom.vue";
import EmailTableHeaderAtom from "@/atoms/users/textelements/tableheaders/EmailTableHeaderAtom.vue";
import FirstNameTableHeaderAtom from "@/atoms/users/textelements/tableheaders/FirstNameTableHeaderAtom.vue";
import LastNameTableHeaderAtom from "@/atoms/users/textelements/tableheaders/LastNameTableHeaderAtom.vue";
import PhoneNumberTableHeaderAtom from "@/atoms/users/textelements/tableheaders/PhoneNumberTableHeaderAtom.vue";

</script>

<template>
    <tr class="table table-light">
        <FirstNameTableHeaderAtom />
        <LastNameTableHeaderAtom />
        <EmailTableHeaderAtom />
        <BSNNumberTableHeaderAtom />
        <PhoneNumberTableHeaderAtom />
        <ActionsTableHeaderAtom />
    </tr>
</template>