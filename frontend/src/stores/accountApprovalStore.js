import { defineStore } from "pinia";
import { ref } from "vue";

export const useAccountApprovalStore = defineStore('accountApproval', () => {
    const selectedUserId = ref(null);
    const pendingBankAccounts = ref({
        checking: null, 
        savings: null
    });
    function initApproval(userId) {
        selectedUserId.value = userId;
        pendingBankAccounts.value = {
            checking: createAccount("checking", selectedUserId.value),
            savings: createAccount("savings", selectedUserId.value)
        }
    }

    function clearApproval() {
        selectedUserId.value = null;
        pendingBankAccounts.value = {checking: null, savings: null};
    }

    function generateIban(type) {
        const base = "NL";
        const bank = "INHO0";

        let typeDigit;
        if(type === "checking")
        {
            typeDigit = "1";
        }
        else
        {
            typeDigit = "2";
        }

        const randompart = generateFixedRandom(9);
        return base + "xx" + bank + typeDigit + randompart ;
    }

    function createAccount(type, userId)
    { 
       const iban = generateIban(type);
       return {
        iban,
        userId,
        bankAccountType: type 
       }
    }

    function generateFixedRandom(length)
    {
        return Math.random().toString().slice(2, 2 + length);
    }

    return {
        selectedUserId,
        pendingBankAccounts,
        initApproval,
        clearApproval, 
        createAccount
    }
})