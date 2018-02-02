package com.project.manutenza;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class PayPalController {

    // Replace these values with your clientId and secret. You can use these to get started right now.
    private String clientId = "AYSq3RDGsmBLJE-otTkBtM-jBRd1TCQwFf9RGfwddNXWz0uFU9ztymylOhRS";
    private String clientSecret = "EGnHDxD_qRPdaLdZz8iCr8N7_MzF-YHPTkjs6NKYQvQSBngp4PTTVWkPZRbL";
    private APIContext apiContext;  //Impostato come attributo per fare l'execute del pagamento

    @RequestMapping("/paypal/checkout")
    public String createPayment(){
        // Set payer details. Non so bene cosa potremmo mettergli
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        // Set redirect URLs. Uno per il cancellamento del pagamento, uno per il processamento
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/paypal/canceled");
        redirectUrls.setReturnUrl("http://localhost:8080/paypal/process");

        // Set payment details. Si mette il prezzo totale, il prezzo della spedizione e le tasse
        //La somma di questi 3 va nell'amount total
        Details details = new Details();
        details.setShipping("3.4");         //Dettagli spedizione
        details.setSubtotal("999995");      //Subtotale di tutti gli items TASSE ESCLUSE
        details.setTax("1.5");              //Totale tasse ITEMS.

        //Set item list. Serve cercare un item per visualizzare i dettagli del pagamento, altrimenti
        //nemmeno spunta quanto vai a pagare.
        Item item1 = new Item();
        item1.setName("Tassa per il Leader Supremo bein");
        item1.setDescription("Se non paghi, Darth Bein verrà a casa tua, e sai quanto la forza è potente in lui");
        item1.setCurrency("EUR");
        item1.setQuantity("1");
        item1.setTax("0.5");
        item1.setPrice("999990");

        Item item2 = new Item();
        item2.setDescription("Biscottini della fortuna dell'Ordine Oscuro.");
        item2.setCurrency("EUR");
        item2.setQuantity("5");
        item2.setTax("0.2");
        item2.setPrice("1");

        ItemList itemList = new ItemList();
        ArrayList<Item> itemArray = new ArrayList<>();
        itemArray.add(item1);
        itemArray.add(item2);
        itemList.setItems(itemArray);

        // Payment amount
        Amount amount = new Amount();
        amount.setCurrency("EUR");
        // Total must be equal to sum of shipping, tax and subtotal.
        amount.setTotal("999999.9");
        amount.setDetails(details);

        // Transaction information
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setItemList(itemList);
        transaction.setDescription("This is the payment transaction description.");

        // Add transaction to a list
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        // Add payment details
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setRedirectUrls(redirectUrls);
        payment.setTransactions(transactions);


        try {
            apiContext = new APIContext(clientId, clientSecret, "sandbox");
            Payment createdPayment = payment.create(apiContext);

            //Il link alla conferma di pagamento sta nell'oggetto Links al secondo posto (capito visualizzando l'intero
            //createdPayment in JSON). Siccome è un link esterno, ho bisogno del redirect
            Iterator links = createdPayment.getLinks().iterator();
            while (links.hasNext()) {
                Links link = (Links) links.next();
                if (link.getRel().equalsIgnoreCase("approval_url")) {
                    //System.out.println(createdPayment.toJSON());
                    return "redirect:"+createdPayment.getLinks().get(1).getHref();
                }
            }
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
            return "error";
        }

        return "error";
    }

    //Metodo richiamato a pagamento avvenuto con successo. I parametri vengono presi da Spring direttamente dall'URL chiamante
    @RequestMapping("/paypal/process")
    @ResponseBody
    public String paypalReturn(String paymentId, String PayerID){
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(PayerID);
        try {
            Payment createdPayment = payment.execute(apiContext, paymentExecution);
            return ""+createdPayment;
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
        }
        return "durante il processamento qualcosa è andato storto.";
    }

    //Metodo richiamato a pagamento annullato
    @RequestMapping("/paypal/canceled")
    @ResponseBody
    public String paypalCanceled(){
        return "pagamento cancellato";
    }

}
