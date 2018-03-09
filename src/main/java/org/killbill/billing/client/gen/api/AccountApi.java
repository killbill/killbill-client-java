package org.killbill.billing.client.gen.api;


import org.killbill.billing.client.gen.model.Account;


import com.google.common.collect.Multimap;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.base.MoreObjects;

import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.RequestOptions;
import org.killbill.billing.util.api.AuditLevel;

public class AccountApi {

    private final KillBillHttpClient httpClient;

    public AccountApi() {
        this(new KillBillHttpClient());
    }

    public AccountApi(final KillBillHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Account createAccount(final Account body,  final RequestOptions inputOptions) throws KillBillClientException {
        Preconditions.checkNotNull(body, "Missing the required parameter 'body' when calling createAccount");

        final String uri = "/1.0/kb/accounts";


        final Boolean followLocation = MoreObjects.firstNonNull(inputOptions.getFollowLocation(), Boolean.TRUE);
        final RequestOptions requestOptions = inputOptions.extend()
            .withFollowLocation(followLocation)
            .build();

        return httpClient.doPost(uri, body, Account.class, requestOptions);
    }

    public Account getAccountByKey(final String externalKey, final Boolean accountWithBalance, final Boolean accountWithBalanceAndCBA, final AuditLevel auditLevel,  final RequestOptions inputOptions) throws KillBillClientException {

        Preconditions.checkNotNull(externalKey, "Missing the required parameter 'externalKey' when calling getAccountByKey");

        final String uri = "/1.0/kb/accounts";

        final Multimap<String, String> queryParams = HashMultimap.<String, String>create(inputOptions.getQueryParams());
        queryParams.put("externalKey", String.valueOf(externalKey));
        queryParams.put("accountWithBalance", String.valueOf(accountWithBalance));
        queryParams.put("accountWithBalanceAndCBA", String.valueOf(accountWithBalanceAndCBA));
        queryParams.put("auditLevel", String.valueOf(auditLevel));

        final RequestOptions requestOptions = inputOptions.extend().withQueryParams(queryParams).build();

        return httpClient.doGet(uri, Account.class, requestOptions);
    }

}
