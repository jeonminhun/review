    function likeReview(button) {
        const reviewId = button.getAttribute("data-review-id");
        const userId = button.getAttribute("data-user-id");
        const isCurrentlySaved = button.classList.contains("liked");
        fetch(`/reviewLike`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                review:{review_id: reviewId},
                user:{user_id:userId},
                review_ch:1
            })
        }).then(response => response.json())
          .then(data => {
              if (data != null) {
                  button.querySelector("span").innerText = data.likeCount; // 좋아요 수 업데이트
                   // 좋아요가 눌렸으면 버튼 상태 변경
                  if (!isCurrentlySaved) {
                    alert("좋아요 성공!");
                    button.classList.add("liked");
                   } else {
                    alert("좋아요 해제!");
                    button.classList.remove("liked");
                  }

                  // ✅ 강제로 DOM 다시 그리기 (리렌더링)
                              button.style.display = 'none';
                              setTimeout(() => button.style.display = 'inline-block', 10);
              } else {
                  alert("좋아요 실패!");
              }
          }).catch(error => console.error("Error:", error));
    }