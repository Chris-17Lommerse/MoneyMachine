package MoneyMachine.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import MoneyMachine.services.interfaces.BankAccountService;

@Controller
@RequestMapping("bankAccounts")
public class BankAccountController extends BaseController
{
    private BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService)
    {
        this.bankAccountService = bankAccountService;
    }
}