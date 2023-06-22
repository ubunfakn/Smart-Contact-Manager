$( document ).ready(function() {
    $(".sidebar").hide();
    $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");
    
});
const toggleSidebar=()=>{
    if($('.sidebar').is(":visible"))
    {
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");
    }else
    {
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "19%");
    }
}

const search=()=>{
    console.log("searching");
    let query=$("#search").val();
    if(query==="")
    {
        $(".search-result").hide();
    }
    else
    {

        let url=`http://localhost:8080/search/${query}`;

        fetch(url).then((response)=>{
            return response.json();
        }).then((data) => {

            let text=`<div class='list-group'>`

            data.forEach((contact) => {
                text+=`<a href='/user/contact/${contact.cid}' class='list-group-item list-group-item-action'>${contact.name}</a>`
            });
            text+=`</div>`

            $(".search-result").html(text);
            $(".search-result").show();
        })
    }
}

//First request to server to create order

const paymentStart=()=>{
    console.log('hi');
    let amount = $("#paymentfield").val()
    console.log(amount);
    if(amount=='' || amount==null)
    {
        alert("Please enter some amount");
        return;
    }
    else
    {
        //We are using ajax to send request to server to create order jQuery AJAX
        $.ajax(
            {
                url: '/user/create_order',
                data:JSON.stringify({amount:amount,info:'order_request'}),
                contentType:'application/json',
                type:'POST',
                dataType:'json',
                success:function(response){
                    //invoked when success
                    console.log(response);
                    if(response.status==='created')
                    {
                        // open payment form
                        let options={
                            key:'rzp_test_PFH56BsCj9Z0Ty',
                            amount:response.amount,
                            currency:'INR',
                            name:'Smart Contact Manager',
                            description:'We want our right',
                            image:'https://m.media-amazon.com/images/I/71eOAWo45OL.png',
                            order_id:response.id,
                            handler:function(response){
                                // success response
                                console.log(response.razorpay_payment_id);
                                console.log(response.razorpay_order_id);
                                console.log(response.razorpay_signature);
                                console.log("payment successfull");
                                updatePaymentOnServer(response.razorpay_payment_id,response.razorpay_order_id,"paid");
                                

                            },
                            prefill:{
                                "name":"",
                                "email": "",
                                "contact":"",
                            },
                            notes:{
                                address:'UBUNFAKN'
                            },
                            theme:{
                                color:'#F37254'
                            }
                        };
                        let rzp=new Razorpay(options);
                        rzp.on("payment.failed", function(response){
                            // payment failed
                            console.log(response);
                            console.log("Payment failed");
                            console.log(response.error.code);
                            console.log(response.error.description);
                            console.log(response.error.source);
                            console.log(response.error.step);
                            console.log(response.error.reason);
                            console.log(response.error.metadata.order_id);
                            console.log(response.error.metadata.payment_id);
                            swal("Sorry!", "Payment failed", "error");
                        });
                        rzp.open();

                    }
                },
                error:function(error){
                    //invoked when error
                    console.log(error);
                    alert("Something went wrong!")
                }
            }
        )
    }
}
function updatePaymentOnServer(payment_id,order_id,statuses)
{
    $.ajax(
        {
            url: '/user/update_order',
            data:JSON.stringify({payment_id:payment_id,order_id:order_id, statuses:statuses}),
            contentType:'application/json',
            type:'POST',
            dataType:'json',
            success:function(response){
                swal("Good job!", "Payment Ho gayi", "success");
            },
            error:function(error){
                swal("Sorry!", "Payment is successfull but we did not get on server, we will resolve it as soon as possible", "error");
            },
        });
}