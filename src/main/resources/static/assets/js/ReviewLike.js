    function likeReview(button) {
        const reviewId = button.getAttribute("data-review-id");

        fetch(`/review/${reviewId}/like`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            }
        }).then(response => response.json())
          .then(data => {
              if (data.success) {
                  button.querySelector("span").innerText = data.likeCount; // 좋아요 수 업데이트
              } else {
                  alert("좋아요 실패!");
              }
          }).catch(error => console.error("Error:", error));
    }

    function dislikeReview(button) {
        const reviewId = button.getAttribute("data-review-id");

        fetch(`/review/${reviewId}/dislike`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            }
        }).then(response => response.json())
          .then(data => {
              if (data.success) {
                  button.querySelector("span").innerText = data.dislikeCount; // 싫어요 수 업데이트
              } else {
                  alert("싫어요 실패!");
              }
          }).catch(error => console.error("Error:", error));
    }
