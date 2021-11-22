package com.golightyear.backend.transaction

import com.fasterxml.jackson.databind.ObjectMapper
import com.golightyear.backend.AbstractIntegrationSpec
import com.golightyear.backend.account.AccountRepository
import com.golightyear.backend.account.domain.Account
import com.golightyear.backend.account.domain.AccountId
import com.golightyear.backend.account.domain.AccountName
import com.golightyear.backend.currency_account.CurrencyAccountRepository
import com.golightyear.backend.currency_account.domain.CurrencyAccount
import com.golightyear.backend.currency_account.domain.CurrencyAccountId
import com.golightyear.backend.testdata.AccountTestData
import com.golightyear.backend.transaction.domain.FundsTransactionCreateRequest
import com.golightyear.core.Currency
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.util.NestedServletException

import static com.golightyear.backend.account.domain.AccountState.INACTIVE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class TransactionControllerIntSpec extends AbstractIntegrationSpec {

    @Autowired
    protected MockMvc mvc

    @Autowired
    AccountRepository accountRepository

    @Autowired
    CurrencyAccountRepository currencyAccountRepository

    @Autowired
    ObjectMapper objectMapper

    def "money is credited to currency account"() {
        given:
            Account account = addNewAccount()
            CurrencyAccount currencyAccount = addNewCurrencyAccount(Currency.EUR, new BigDecimal("10"), account.id())

            FundsTransactionCreateRequest request = new FundsTransactionCreateRequest(
                account.id().value().toString(),
                currencyAccount.id().value().toString(),
                new BigDecimal("5"),
                ["EE12345", "John snow"]
            )

        expect:
            String requestJson = objectMapper.writeValueAsString(request)
            mvc
                .perform(
                    post('/transaction/credit')
                        .content(requestJson)
                        .contentType("application/json")
                )
                .andExpect(status().isOk())

            CurrencyAccount updatedCurrencyAccount = currencyAccountRepository.find(currencyAccount.id()).get()
            updatedCurrencyAccount.amount() == new BigDecimal("15")
    }

    def "money is can not be credited if currency account does not exist"() {
        given:
            Account account = addNewAccount()

            FundsTransactionCreateRequest request = new FundsTransactionCreateRequest(
                account.id().value().toString(),
                UUID.randomUUID().toString(),
                new BigDecimal("5"),
                ["EE12345", "John snow"]
            )

        when:
            String requestJson = objectMapper.writeValueAsString(request)
            mvc
                .perform(
                    post('/transaction/credit')
                        .content(requestJson)
                        .contentType("application/json")
                )

        then:
            thrown NestedServletException
    }

    def "money is can not be debited if currency account does not exist"() {
        given:
            Account account = addNewAccount()

            FundsTransactionCreateRequest request = new FundsTransactionCreateRequest(
                account.id().value().toString(),
                UUID.randomUUID().toString(),
                new BigDecimal("5"),
                ["EE12345", "John snow"]
            )

        when:
            String requestJson = objectMapper.writeValueAsString(request)
            mvc
                .perform(
                    post('/transaction/debit')
                        .content(requestJson)
                        .contentType("application/json")
                )

        then:
            thrown NestedServletException
    }

    def "money is debited from currency account"() {
        given:
            Account account = addNewAccount()
            CurrencyAccount currencyAccount = addNewCurrencyAccount(Currency.EUR, new BigDecimal("10"), account.id())

            FundsTransactionCreateRequest request = new FundsTransactionCreateRequest(
                account.id().value().toString(),
                currencyAccount.id().value().toString(),
                new BigDecimal("10"),
                ["EE12345", "John snow"]
            )

        expect:
            String requestJson = objectMapper.writeValueAsString(request)
            mvc
                .perform(
                    post('/transaction/debit')
                        .content(requestJson)
                        .contentType("application/json")
                )
                .andExpect(status().isOk())

            CurrencyAccount updatedCurrencyAccount = currencyAccountRepository.find(currencyAccount.id()).get()
            updatedCurrencyAccount.amount() == new BigDecimal("0")
    }

    def "money can not be debited if amount exceeds existing"() {
        given:
            Account account = addNewAccount()
            CurrencyAccount currencyAccount = addNewCurrencyAccount(Currency.EUR, new BigDecimal("10"), account.id())

            FundsTransactionCreateRequest request = new FundsTransactionCreateRequest(
                account.id().value().toString(),
                currencyAccount.id().value().toString(),
                new BigDecimal("11"),
                ["EE12345", "John snow"]
            )

        when:
            String requestJson = objectMapper.writeValueAsString(request)
            mvc
                .perform(
                    post('/transaction/debit')
                        .content(requestJson)
                        .contentType("application/json")
                )
                .andExpect(status().isOk())

        then:
            thrown NestedServletException
    }

    Account addNewAccount() {
        Account account = new AccountTestData(
            id: new AccountId(UUID.randomUUID()),
            name: new AccountName("some inactive account"),
            state: INACTIVE,
        ).build()
        accountRepository.add(account)

        return account
    }

    CurrencyAccount addNewCurrencyAccount(Currency currency, BigDecimal amount, AccountId accountId) {
        CurrencyAccount currencyAccount = new CurrencyAccount(
            id: new CurrencyAccountId(UUID.randomUUID()),
            accountId: accountId,
            currency: currency,
            amount: amount
        )
        currencyAccountRepository.add(currencyAccount)

        return currencyAccount
    }
}
