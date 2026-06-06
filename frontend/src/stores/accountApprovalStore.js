import { defineStore } from "pinia";
import { ref } from "vue";
import { useRouter } from "vue-router";
const router = useRouter();

export const useAccountApprovalStore = defineStore('accountApproval', () => {
    const selectedUserId = ref(null);
    const pendingBankAccounts = ref({
        checking: null,
        savings: null
    });
    function initApproval(userId) {
        if(!userId)
        {
            throw new Error("userId  is required for approval flow")
            router.push({
                path: "/users"
            })
        }
        selectedUserId.value = userId;
        pendingBankAccounts.value = {
            checking: createAccount("CHECKING", selectedUserId.value, 0, 100, 500, -100),
            savings: createAccount("SAVINGS", selectedUserId.value, 0, 2000, 10000, 0)
        }
    }

    function clearApproval() {
        selectedUserId.value = null;
        pendingBankAccounts.value = { checking: null, savings: null };
    }

    function generateIban(type) {
        const base = "NL";
        const bank = "INHO0";

        let typeDigit;
        if (type === "CHECKING") {
            typeDigit = "1";
        }
        else {
            typeDigit = "2";
        }

        const randompart = generateFixedRandom(9);
        return base + "xx" + bank + typeDigit + randompart;
    }

    function createAccount(type, userId, balance, singleTransferLimit, dailyTransferLimit, absoluteLimit) {
        const iban = generateIban(type);
        return {
            iban,
            userId,
            bankAccountType: type,
            balance,
            singleTransferLimit,
            dailyTransferLimit,
            absoluteLimit
        }
    }

    function generateFixedRandom(length) {
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