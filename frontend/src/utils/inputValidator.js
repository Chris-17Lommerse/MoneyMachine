function throwIfMoneyAmountIsNotValid(amount, singleTransferLimit) {
    
    if (amount <= 0) {
        throw new Error('Amount cannot be less or equal to 0.')
    }

    if (amount > singleTransferLimit) {
        throw new Error('Amount cannot be more than the single transfer limit.')
    }
}

function throwIfWithdrawAmountIsNotValid(amount, totalAmount, absoluteLimit) {

    if (totalAmount - amount < absoluteLimit) {
        throw new Error('Total amount cannot be less the absolute limit.')
    }
}

export { throwIfMoneyAmountIsNotValid, throwIfWithdrawAmountIsNotValid }