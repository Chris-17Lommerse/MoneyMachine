function getPriceFormatted(price){

    return "€ " + Number(price).toLocaleString('nl-NL', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
    });    
}

export { getPriceFormatted };